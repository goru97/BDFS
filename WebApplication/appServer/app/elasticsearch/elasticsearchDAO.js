/**
 * Created by mjain
 *
 * https://blog.raananweber.com/2015/11/24/simple-autocomplete-with-elasticsearch-and-node-js/
 */


var elasticsearch = require('elasticsearch');

var elasticClient = new elasticsearch.Client({
    host: 'localhost:9200',
    log: 'info'
});

var indexName = "hids";

/**
 * Delete an existing index
 */
function deleteIndex() {
    return elasticClient.indices.delete({
        index: indexName
    });
}
exports.deleteIndex = deleteIndex;

/**
 * create the index
 */
function initIndex() {
    return elasticClient.indices.create({
        index: indexName
    });
}
exports.initIndex = initIndex;

/**
 * check if the index exists
 */
function indexExists() {
    return elasticClient.indices.exists({
        index: indexName
    });
}
exports.indexExists = indexExists;


function addUser(id, username, password) {
    return elasticClient.index({
        index: "webapp",
        type: "users",
        body: {
            tenantID: id,
            username: username,
            content: password
        }
    });
}

exports.addUser = addUser;


function initMapping() {
    return elasticClient.indices.putMapping({
        index: indexName,
        type: "document",
        body: {
            properties: {
                title: { type: "string" },
                content: { type: "string" },
                suggest: {
                    type: "completion",
                    analyzer: "simple",
                    search_analyzer: "simple",
                    payloads: true
                }
            }
        }
    });
}
exports.initMapping = initMapping;

function addDocument(document) {
    return elasticClient.index({
        index: indexName,
        type: "document",
        body: {
            title: document.title,
            content: document.content,
            suggest: {
                input: document.title.split(" "),
                output: document.title,
                payload: document.metadata || {}
            }
        }
    });
}
exports.addDocument = addDocument;

function getSuggestions(input) {
    return elasticClient.suggest({
        index: indexName,
        type: "document",
        body: {
            docsuggest: {
                text: input,
                completion: {
                    field: "suggest",
                    fuzzy: true
                }
            }
        }
    })
}
exports.getSuggestions = getSuggestions;

/**
 * Search for a document
 */

function searchData(input){

    return elasticClient.search({
        index: indexName,
        type: "document",
        body: {
            query: {
                match: {
                    title: input
                }
            }
        }
    })
}

exports.searchData = searchData;

