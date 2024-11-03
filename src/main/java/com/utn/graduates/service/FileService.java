package com.utn.graduates.service;

import com.google.common.base.Preconditions;
import com.utn.graduates.model.ContactType;
import com.utn.graduates.constants.Genre;
import com.utn.graduates.exception.FileException;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.model.Specialty;
import com.utn.graduates.repository.GraduateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private static final String DNI = "dni";
    private static final String FULLNAME = "fullname";
    private static final String GENRE = "genre";
    private static final String SPECIALTY = "specialty";
    private static final String CONTACT_TYPE = "contact_type";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    private static final String[] requiredCsvColumns = {DNI, FULLNAME, GENRE, CONTACT_TYPE, SPECIALTY, EMAIL};
    private static final String SEMICOLON = ";";
    private static final String COMMA = ",";

    private final GraduateRepository graduateRepository;
    private final SpecialtyService specialtyService;
    private final ContactTypeService contactTypeService;

    public FileService(GraduateRepository graduateRepository, SpecialtyService specialtyService, ContactTypeService contactTypeService) {
        this.graduateRepository = graduateRepository;
        this.specialtyService = specialtyService;
        this.contactTypeService = contactTypeService;
    }

    @Transactional
    public int importGraduatesFromCsv(final MultipartFile file) {
        Set<Graduate> graduates = new HashSet<>();
        Set<String> existingDni = graduateRepository.findAllDni();
        Set<String> csvDni = new HashSet<>();
        List<String> specialties = this.specialtyService.getSpecialties();
        List<String> contactTypes = this.contactTypeService.getContactTypes();

        int lineNumber = 1;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            this.validateCsvColumns(line.toLowerCase());
            Map<String, String> graduateData = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] fields = line.split(SEMICOLON);
                Preconditions.checkState(fields.length == requiredCsvColumns.length, "CSV format error: mismatched number of mandatory fields in line: " + line);

                for (int i = 0; i < requiredCsvColumns.length; i++) {
                    graduateData.put(requiredCsvColumns[i], fields[i].trim());
                }
                String dni = graduateData.get(DNI);
                Preconditions.checkState(!csvDni.contains(dni), "The register is duplicated in the file, please check the csv file at line: " + lineNumber);
                Preconditions.checkState(!existingDni.contains(dni), String.format("The register in line %s already exists", lineNumber));
                Graduate graduate = this.convertToEntity(graduateData, dni);
                this.checkField(graduate.getSpecialty().getName(), specialties);
                this.checkField(graduate.getContactType().getName(), contactTypes);

                graduates.add(graduate);
                csvDni.add(dni);
            }

            Preconditions.checkState(!CollectionUtils.isEmpty(graduates), "The registers to save are empty.");
            graduateRepository.saveAll(graduates);
            LOGGER.info("saved successfully {} registers", graduates.size());
        } catch (Exception e) {
            LOGGER.error("Failed to import graduates from csv file", e);
            throw new FileException(String.format("Failed to import CSV file. Error: %s", e));
        }

        return graduates.size();
    }

    private void checkField(final String field, final List<String> list) {
        Preconditions.checkState(!CollectionUtils.isEmpty(list), "The field " + field + " doesn´t exists, please create it first");
        Preconditions.checkState(list.contains(field), "The field " + field + " doesn´t exists, please create it first");
    }

    private Graduate convertToEntity(final Map<String, String> graduateData, final String dni) {
        Graduate graduate = new Graduate();
        graduate.setFullname(graduateData.get(FULLNAME));
        graduate.setDni(dni);
        graduate.setPhone(graduateData.get(PHONE));
        graduate.setEmail(graduateData.get(EMAIL));
        graduate.setGenre(Genre.valueFromFields(graduateData.get(GENRE)));
        graduate.setContactType(new ContactType(graduateData.get(CONTACT_TYPE)));
        graduate.setSpecialty(new Specialty(graduateData.get(SPECIALTY)));
        return graduate;
    }

    private void validateCsvColumns(String line) {
        Preconditions.checkState(StringUtils.hasText(line), "Missing columns in csv file");
        Preconditions.checkState(!line.contains(COMMA), "Invalid csv format, the columns canot be separated by ',' " + line);
        String[] columns = line.split(SEMICOLON);
        List<String> presentColumns = Arrays.asList(columns);
        for (String requiredColumn : requiredCsvColumns) {
            Preconditions.checkState(presentColumns.contains(requiredColumn), "Missing required column: " + requiredColumn);
        }
    }
}
