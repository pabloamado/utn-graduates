package com.utn.graduates.service;

import com.utn.graduates.constants.ContactType;
import com.utn.graduates.constants.Genre;
import com.utn.graduates.error.CustomResponse;
import com.utn.graduates.model.Graduate;
import com.utn.graduates.repository.GraduateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    private static final String CONTACT_TYPE = "contact_type";
    private static final String[] requiredCsvColumns = {DNI, FULLNAME, GENRE, CONTACT_TYPE};
    private static final String SEMICOLON = ";";

    private GraduateRepository graduateRepository;

    public FileService(GraduateRepository graduateRepository) {
        this.graduateRepository = graduateRepository;
    }

    @Transactional
    public CustomResponse importGraduatesFromCsv(final MultipartFile file) {
        Set<Graduate> graduates = new HashSet<>();
        Set<String> existingDni = graduateRepository.findAllDni();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            validateCsvColumns(line);

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(SEMICOLON);

                if (fields.length != requiredCsvColumns.length) {
                    throw new IllegalArgumentException("CSV format error: mismatched number of fields in line: " + line);
                }

                Map<String, String> graduateData = new HashMap<>();
                for (int i = 0; i < requiredCsvColumns.length; i++) {
                    graduateData.put(requiredCsvColumns[i], fields[i].trim());
                }

                String dni = graduateData.get(DNI);
                if (!existingDni.contains(dni)) {
                    Graduate graduate = new Graduate();
                    graduate.setFullname(graduateData.get(FULLNAME));
                    graduate.setDni(dni);
                    graduate.setGenre(Genre.valueFromFields(graduateData.get(GENRE)));
                    graduate.setContactType(ContactType.valueFromTranslation(graduateData.get(CONTACT_TYPE)));
                    graduates.add(graduate);
                }

            }
            if(CollectionUtils.isEmpty(graduates)){
                throw new IllegalArgumentException("The collection of graduates is empty.");
            }
            graduateRepository.saveAll(graduates);
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.BAD_REQUEST, String.format("Failed to import CSV file. Error: %s", e));
        }
        return new CustomResponse(HttpStatus.OK,String.format("Registers saved %s", graduates.size()));
    }

    private void validateCsvColumns(String line) {
        if (StringUtils.hasText(line)) {
            String[] columns = line.split(SEMICOLON);
            List<String> presentColumns = Arrays.asList(columns);
            for (String requiredColumn : requiredCsvColumns) {
                if (!presentColumns.contains(requiredColumn)) {
                    throw new IllegalArgumentException("Missing required column: " + requiredColumn);
                }
            }
        } else {
            throw new IllegalArgumentException("Missing columns in csv file");
        }

    }
}
