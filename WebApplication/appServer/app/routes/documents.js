/**
 * Created by mjain
 * */

var express = require('express');
var router = express.Router();

var elastic = require('../elasticsearch/elasticsearchDAO');

/* GET suggestions */
router.get('/suggest/:input', function (req, res, next) {
    elastic.getSuggestions(req.params.input).then(function (result) { res.json(result) });
});

/* POST document to be indexed */
router.post('/', function (req, res, next) {
    elastic.addDocument(req.body).then(function (result) { res.json(result) });
});

/**
 * GET document
 */
router.get('/search/:input', function (req, res, next) {
    elastic.searchData(req.params.input).then(function (result) { res.json(result.hits.hits) });
});


module.exports = router;
