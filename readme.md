# HTTP/REST Echo Service

The HTTP/REST Echo Service is a lightweight web service designed to simplify the testing of HTTP or
REST clients. By sending requests to this service, users can easily inspect and verify the
transmitted data. The service echoes back all request headers and the request body, providing a
quick and convenient way to validate the information sent from your client.

[![Java Version][java-version]][jdk-download]
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0.html)  
[![GitHub master check runs][github-master-check-runs]][github-master-check-runs-link]
![GitHub commit activity][github-commit-activity]
[![Today's hits][today-hits]][today-hits-link]

## Features

* Echoes all request headers.
* Echoes the request body.

## How to Use

Send your HTTP or REST requests to the provided service endpoint,
see https://cutt.ly/http-echo-faas. Receive a detailed response containing all the request headers
and body.

This service is invaluable for debugging, testing, and ensuring the accuracy of your client's
interactions with APIs or web services.

[java-version]: https://img.shields.io/static/v1?label=Java&message=11&color=blue&logoColor=E23D28

[jdk-download]: https://www.oracle.com/java/technologies/downloads/#java11

[github-master-check-runs]: https://img.shields.io/github/check-runs/vitalijr2/http-echo-faas/main

[github-master-check-runs-link]: https://github.com/vitalijr2/http-echo-faas/actions?query=branch%3Amain

[github-commit-activity]: https://img.shields.io/github/commit-activity/y/vitalijr2/http-echo-faas

[today-hits]: https://hits.sh/github.com/vitalijr2/http-echo-faas.svg?view=today-total&label=today's%20hits

[today-hits-link]: https://hits.sh/github.com/vitalijr2/http-echo-faas/
