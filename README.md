# ElasticSearchApp
a simple elasticsearch app for fast searching of data. 
The Project was initialized using *spring 
initializer* which autoconfigured all required dependencies via **Maven**.

### Getting Started
Start ElasticSearch using docker

To set up the docker compose - </br>
1. Make sure docker is installed and running. Then run 'docker-compose up -d'  in your terminal. 
This command will start an Elasticsearch container locally on port 9200.
2. To verify, visit http://localhost:9200 in your browser or postman. 
You should see a JSON response from Elasticsearch if it's working correctly.

### Bulk-Indexing

When you **start the application**, bulk indexing is automatically triggered.
This loads data from a predefined JSON file into the **Elasticsearch database**. </br>
You can verify that the data has been successfully indexed by hitting the following GET endpoint using Postman or your browser:
</br>http://localhost:9200/coursedocument/_search?pretty </br>
This will return all indexed documents in a pretty-printed JSON format (with default pagination).

### Example API Usage
Use the **/api/search** endpoint to search for courses with various filters and combinations.

1. Basic Search By keyword (Title/Description) </br>
curl - GET http://localhost:8081/api/search?q=Science </br>
Expected Behavior: </br>
Returns all courses with "Science" in the title or description.</br>
JSON includes total count of matches (which should be 5 in this case) and paginated result set. The JSON should contain the following elements -

"id": "4", </br>
"title": "Science Fest",</br>
"id": "29",</br>
"title": "DIY Science Fair",</br>
"id": "42",</br>
"title": "Science Magic Club",</br>
"id": "47",</br>
"title": "Science Fair Prep Workshop",</br>
"id": "18",</br>
"title": "Science Club",</br>
"total": 5</br>



2. Search By type (sorted)</br>
curl - GET http://localhost:8081/api/search?type=ONE_TIME&sort=priceDesc </br>
Expected Behavior: </br>
Returns all courses with type "ONE_TIME" and properly sorted in descending order w.r.t. price.</br>
JSON includes total count of matches (which should be 12 in this case) and paginated result set.Search By type. The JSON should contain the following elements -

"id": "12",</br>
"title": "Robotics Camp",</br>
"id": "23",</br>
"title": "Space Explorers Camp",</br>
"id": "16",</br>
"title": "Astronomy Night",</br>
"id": "29",</br>
"title": "DIY Science Fair",</br>
"id": "47",</br>
"title": "Science Fair Prep Workshop",</br>
"id": "17",</br>
"title": "Maker Fair 2025",</br>
"id": "26",</br>
"title": "Art & Craft Bonanza",</br>
"id": "35",</br>
"title": "Astronomy Night",</br>
"id": "39",</br>
"title": "Environment Day Special",</br>
"total": 12</br>


3. Search by category (greater than given age) </br>
curl - GET http://localhost:8081/api/search?minAge=10&category=Music </br>
Expected Behavior: </br>
Returns all courses that are in "Music" category and filter that have minAge greater than "10".</br>
JSON includes total count of matches (which should be 2 in this case) and paginated result set. The JSON should contain the following elements -

"id": "1",</br>
"title": "Intro to Guitar",</br>
"id": "46",</br>
"title": "Music Production 101",</br>
"total": 2</br>



## Bonus 

### 1. Autocompletion Suggestion
curl - GET http://localhost:8081/api/suggest?partial=Science </br>
Expected Behavior: </br>
Returns a JSON array of all the titles with Science as there prefix
JSON includes all the title names. For this curl it should be -
[</br>
"Science Club",</br>
"Science Fair Prep Workshop",</br>
"Science Fest",</br>
"Science Magic Club"</br>
]


### 2. Fuzzy Search

This is done in the same curl as the searching one.
To test this enhancement in your Postman hit the endpoint - </br>
curl - GET http://localhost:8081/api/search?q=ciding </br>
(The word 'coding' here is misspelled as 'ciding')
Expected Behavior: </br>
Returns a JSON array of all the titles with Coding in their titles
JSON includes all the title names(there should be 2 elements in this case).The JSON should contain the following elements -


"id": "7",</br>
"title": "Coding for Beginners",</br>
"id": "36",</br>
"title": "Coding Bootcamp for Beginners",</br>
"total": 2</br>


[PS - the lombok in my intellij is apparently not working. For which reason, I have made all the necessary constructors 
and getters/setters in my entity. My guess is maven is not connected to the java version 17 even though it is clearly there in the path variable.
For some reason it is still connected version 11. But that is for me to look into!] </br>
[Another PS - Hope you liked what i have made and faced little to no difficulties in any way]



#### Acknowledgments

Really enjoyed building this assignment with the very interesting but enough deadline! I learned a lot
through this project!

