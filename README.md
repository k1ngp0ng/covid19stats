## Spring boot application that parse COVID-19 datas and push into elasticsearch and provider for KPI dashboards in Kibana

The data are retrieved from this github project : https://github.com/CSSEGISandData/COVID-19
Clone the project on master branch, the data are here csse_covid_19_data/csse_covid_19_daily_reports/ 
  

##### Before inserting documents, set the index configuration for specific data types : geopoint

- index name : covidstats

```
PUT http://elasticurl/covidstats
{
  "mappings": {
    "properties": {
      "location": {
        "type": "geo_point"
      }
    }
  }
}
```

##### Launch the project
```
./mvnw clean compile spring-boot:run
```

##### Start import in elastic
Prerequisite : 
- elasticsearch instance up
- application properties configured to be connected with elasticsearch instance


```
curl http://127.0.0.1:8080/api/covid/load
```

