const path = require("path");
const fs = require('fs')
const axios = require('axios')
require("yargonaut")
    .font('Roboto')
    .style('blue')
    .style('yellow.italic','required')
    .helpStyle('greeb')
    .errorsStyle('red.bold');

class HarbourFileImporter {

    constructor(options) {

        // override console.debug to do nothing if debug is disabled
        if (!options['debugLog']){
            console.debug = () => {}
        }
    }

    static ENV_URLS = {
        "production": "https://api.freightmate.com",  //todo confirm this is correct once prod is setup
        "staging": "https://api.uat.staging.freightmate.com",
        "development": "http://localhost:8098"
    }

    static ENDPOINTS = {
        "zones": {
            url: "/api/v1/carrier-zone",
            contentType: "text/csv"
        },
        "ratecards": {
            url: "/api/v1/ratecard",
            contentType: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        },
        "suburbs": {
            url: "/api/v1/suburb",
            contentType: "text/csv"
        }
    }

    readFileLocal = (filePath) => {
        const bytes = fs.readFileSync(filePath, {encoding: 'base64'});

        if (!bytes || bytes.length === 0) {
            throw new Error("File does not exist, or contains no content: " + filePath)
        }

        return bytes
    }


    sendFile = (filePath, endpoint, token, environment = "development") => {
        const bytes = this.readFileLocal(filePath)
        const url = HarbourFileImporter.ENV_URLS[environment] + HarbourFileImporter.ENDPOINTS[endpoint]['url']

        return axios.post(
            url,
            {
                content: bytes,
                filename: path.basename(filePath),
                "Content-Type": HarbourFileImporter.ENDPOINTS[endpoint]['contentType']
            },
            {
                headers: {
                    "authorization": token,
                    "Content-Type": "application/json"
                }
            }
        );
    }
}

module.exports.HarbourFileImporter = HarbourFileImporter
