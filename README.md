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

##### Start import in elastic
Prerequisite : 
- elasticsearch instance up
- application properties configured to be connected with elasticsearch instance
- change ```covid.csv.directory``` key in application.properties to the folder ```$COVID-19_HOME/csse_covid_19_data/csse_covid_19_daily_reports```

##### Launch the project
Prerequisite :   

JDK14 but there's no specific feature of JDK14 needed, so you can downgrade the ```java.version``` property in the pom.xml 
```
./mvnw clean compile spring-boot:run
```

then API call for import data in elasticsearch

```
curl http://127.0.0.1:8080/api/covid/load
```

##### Once the data have been injected in elastic 
Prerequisite : 
- elasticsearch instance up
- kibana up and connected to local elasticsearch instance (defaut configuration for localhost installation)

Import dashboard object and index pattern in kibana

Management > Saved objects, then import and upload into kibana ```kibana/dashboard_objetcs_and_index_pattern.ndjson``` file located inside this project