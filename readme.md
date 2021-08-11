# JSON Aggregator

![build](https://github.com/UL-SnT-Serval/json-aggregator/workflows/build/badge.svg)
![codecov](.github/badges/jacoco.svg)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Description

JSON Aggregator is a little util program that allows to merge different json file in one unique JSON files based on a set of user defined rules.

## Requirements

### To run

* Java Runtime Environement (JRE) 11 or higher

### To build

* Java Development Kit (JDK) 11 or higher
* Maven 3.6.0 or higher

## Build from source

1. Clone the project on your machine using ```git clone https://github.com/UL-SnT-Serval/json-aggregator.git``` .
2. Move to the directory.
3. run the Maven command ```mvn clean package```.

## Run

JSON Aggregator is distributed as a jar file which contains all its dependencies. The name of the JAR file follows the following schema: 

```json-aggregator-{VERSION}-jar-with-dependencies.jar```

To launch the tool, enter the following command:

    java -jar json-aggregator-{VERSION}-jar-with-dependencies.jar \
        -i /path/input/folder \
        -o /path/output/file.csv \
        -f comma,seprated,list

### Attribute

| Short Name| Long Name | Required | Description |
|-----------|-----------|----------|--------------
|`-i`       | `--input` |  YES     | Absolute path to the folder containing the JSON files to merge. Only the files with the extension *.json* are considered.
|`-o`       | `--output`|  YES     | Absolute path to the folder containing the JSON files to merge. The parent folder needs to exists and if the file exists, it is overwritten.
|`-f`       | `--filter`|  NO      | Comma separated list of all the field to keep in the output. If empty, all the fields are kept.

## License

Code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).