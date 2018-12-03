# API - AccessToJson
Convert a Access file into a JSON file. Used to export into a file able to import to MongoDB.

## Info
Save the Access Database without Password and proceed with AccessToJson

## Use
### AccessToJson.java

| Modifier and Type | Method | Description |
| --- | --- | --- |
| Singleton | **Constructor** | --- |
| --- | **Method** | --- |
| *void* | parseToJson (String fileToExport) | Create a file to export the JSON file |
| *void* | setColumn (String title, int column) | Create the column with the *title* from the *column* of the Access DB |
| *void* | setGroup (String title, int column) | Create a Group with one column but differents values from *column* of the Access DB |
| *void* | setToJSON() | Read the DB and generate the JSON file from the configurations methods |


## Example

```java
AccessToJson my = AccessToJson.start();

//true - Verbose mode ON.
//false - Verbose mode OFF
my.setVerbose(true);

//Path and name the Table from the DB
my.setPath("User.accdb", "DATA");

//To create Group like:
//  { Key : Value,
//          Value,
//          Value,
//  }
int[] columnas = new int[2];
columnas[0] = 1;
columnas[1] = 2;

//Create a file JSON
my.parseToJson("fileToExport.json");

//To add a Group to JSON file
my.setGroup("INFO", columnas); //Create a JSON: Key "INFO" : [Value (Data from column 1), (Data from column 2)]

//Add a Key and a column value from the DB Access
my.setColumn("Name", 3); //Create a JSON: Key "Name" : Value (Data from column 3)
my.setColumn("Age", 4); //Create a JSON: Key "Name" : Value (Data from column 4)
my.setColumn("Adress", 5); //Create a JSON: Key "Name" : Value (Data from column 5)
my.setColumn("Telefon", 6); //Create a JSON: Key "Name" : Value (Data from column 6)
my.setColumn("Dad", 7); //Create a JSON: Key "Name" : Value (Data from column 7)
my.setColumn("Mum", 8); //Create a JSON: Key "Name" : Value (Data from column 8)
my.setColumn("Register", 9); //Create a JSON: Key "Name" : Value (Data from column 9)

//Read the DB and generate the JSON file from the configurations methods
my.setToJSON();
