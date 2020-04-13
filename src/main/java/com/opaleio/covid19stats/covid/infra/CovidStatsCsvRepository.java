package com.opaleio.covid19stats.covid.infra;

import com.opaleio.covid19stats.covid.application.CovidStats;
import com.opaleio.covid19stats.covid.domain.Coordinates;
import com.opaleio.covid19stats.covid.domain.DailyCovidStat;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CovidStatsCsvRepository implements CovidStats {

    private static final Logger LOGGER = LoggerFactory.getLogger(CovidStatsCsvRepository.class);

    private static final String CHINA_COUNTRY = "China";

    private final String[] headers = {"Province/State","Country/Region","Last Update","Confirmed", "Deaths", "Recovered"};

    private final String csvDirectory;
    private final DateTimeFormatter csvDatePattern;


    public CovidStatsCsvRepository( @Value("${covid.csv.directory}") String csvDirectory) {
        this.csvDirectory = csvDirectory;
        this.csvDatePattern = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }

    @Override
    public List<DailyCovidStat> loadStats() {
        List<String> csvFiles = getSourceFiles();
        LOGGER.info("files found => {}", csvFiles);
        return csvFiles.stream()
                .map(this::loadDataFromCsv)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<DailyCovidStat> loadDailyStats(LocalDate day) {
        String filename = csvDirectory + File.separator + day.format(csvDatePattern) + ".csv";
        return loadDataFromCsv(filename);
    }


    private List<String> getSourceFiles() {
        try {
            return Files.list(Paths.get(csvDirectory))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(".csv"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("Error occurred {}", e);
        }
        return List.of();
    }

    private List<DailyCovidStat> loadDataFromCsv(String fileSource) {
        LOGGER.info("Treating {} file", fileSource);
        Resource resourceFile = new FileSystemResource(fileSource);
        List<DailyCovidStat> dailyCovidStats = new ArrayList<>();
        try(Reader csvFile = new InputStreamReader(resourceFile.getInputStream())) {
            Character csvSeparator = ',';
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    //.withHeader(headers)
                    .withDelimiter(csvSeparator)
                    .withFirstRecordAsHeader()
                    .parse(csvFile);

            LocalDate fileDate = extractDateFromFileSource(fileSource);
            records.forEach(record -> dailyCovidStats.add(buildCovidSingleData(record, fileDate)));
        } catch(Exception e) {
            LOGGER.error("Error when loading covidstats : {}", e.getMessage());
        }
        LOGGER.info("{} covid data in file", dailyCovidStats.size());
        return dailyCovidStats;
    }


    private DailyCovidStat buildCovidSingleData(CSVRecord record, LocalDate date) {
        /* change of csv structure with add multiple region fields and new lat, long format
         new header format : FIPS,Admin2,Province_State,Country_Region,Last_Update,Lat,Long_,Confirmed,Deaths,Recovered,Active,Combined_Key */
        if (date.isAfter(LocalDate.of(2020,3, 21))) {
            return new DailyCovidStat(date, record.get("Country_Region"), record.get("Province_State"),
                    toLong(record.get("Confirmed")), toLong(record.get("Deaths")),
                    toLong(record.get("Recovered")), Coordinates.of(toDouble(record.get("Lat")), toDouble(record.get("Long_"))));
        }
        return buildOldFormat(record, date);
    }

    private DailyCovidStat buildOldFormat(CSVRecord record, LocalDate date) {
        Coordinates coordinates = null;
        /* change of csv structure with add of longitude and latitude fields
         old header format : Province/State,Country/Region,Last Update,Confirmed,Deaths,Recovered */
        if (date.isAfter(LocalDate.of(2020,2, 29))) {
            coordinates = Coordinates.of(toDouble(record.get("Latitude")), toDouble(record.get("Longitude")));
        }
        return new DailyCovidStat(date, convertBadChinaFormat(record.get("Country/Region")), record.get(0),
                toLong(record.get("Confirmed")), toLong(record.get("Deaths")),
                toLong(record.get("Recovered")),coordinates);
    }

    private LocalDate extractDateFromFileSource(String fileSource) {
        String dateString = extractDatePatternFromFileName(fileSource);
        return LocalDate.parse(dateString, csvDatePattern);
    }

    private String extractDatePatternFromFileName(String filename) {
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1).replaceAll(".csv", "");
    }

    private Long toLong(String value) {
        if (value.isEmpty()) {
            return 0L;
        }
        return Long.valueOf(value);
    }

    private Double toDouble(String value) {
        if (value.isEmpty()) {
            return null;
        }
        return Double.valueOf(value);
    }

    private String convertBadChinaFormat(String format) {
        if ("Mainland China".equals(format)) {
            return CHINA_COUNTRY;
        }
        return format;
    }



}
