# Sherlastic Elasticsearch Plugin

_Semantic enrichment for [Elasticsearch](https://www.elastic.co) using [Sherlok](http://sherlok.io)._

## Overview

Sherlastic provides an Elasticsearch plugin to enhance its index with semantic information from [Sherlok](http://sherlok.io). That is, text mining is applied on every document being indexed and on every incoming search query. The extracted semantic information can then be used to improve the relevance of search results.


## Version

| Version   | Elasticsearch |
|:---------:|:-------------:|
| master    | 1.6.X         |


### Issues/Questions

Please file an [issue](https://github.com/sherlok/sherlastic/issues "issue").

## Installation

### Install Sherlastic Plugin

    $ $ES_HOME/bin/plugin --install io.sherlok/sherlastic/1.6.0

## Getting Started

### Add Sherlok Analyzer

First, you need to add a sherlok analyzer when creating your index:

    $ curl -XPUT 'localhost:9200/my_index' -d '{
      "index":{
        "analysis":{
          "analyzer":{
            "sherlok_analyzer":{
              "type":"custom",
              "tokenizer":"standard",
              "filter":["sherlok"]
            }
          }
        }
      }
    }'

Feel free to change `tokenizer`, `char_filter` and `filter` settings, but `sherlok` filter needs to be added as a last filter.

### Add Sherlok field

Put minhash field into an index mapipng:

    $ curl -XPUT "localhost:9200/my_index/my_type/_mapping" -d '{
      "my_type":{
        "properties":{
          "message":{
            "type":"string",
            "copy_to":"sherlok_value"
          },
          "sherlok_value":{
            "type":"sherlok",
            "sherlok_analyzer":"sherlok_analyzer"
          }
        }
      }
    }'


## Get Content created by Sherlok

Add the following document:

    $ curl -XPUT "localhost:9200/my_index/my_type/1" -d '{
      "message":"Fess is Java based full text search server provided as OSS product."
    }'

The minhash value is calculated automatically when adding the document.
You can check it as below:

    $ curl -XGET "localhost:9200/my_index/my_type/1?pretty&fields=sherlok_value,_source" 

The response is:

    {
      "_index" : "my_index",
      "_type" : "my_type",
      "_id" : "1",
      "_version" : 1,
      "found" : true,
      "_source":{
          "message":"Fess is Java based full text search server provided as OSS product."
        },
      "fields" : {
        "sherlok_value" : [ "TODO" ]
      }
    }

