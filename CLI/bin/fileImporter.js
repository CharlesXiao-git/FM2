#!/usr/bin/env node

const yargs = require("yargs");
const axios = require("axios");
const HarbourFileImporter = require("../src/HarbourFileImporter").HarbourFileImporter
const username = process.env.HARBOUR_USER
const password = process.env.HARBOUR_PASSWORD
const SUCCESS = 0
const MISSING_CREDENTIALS = 1
const LOGIN_FAILED = 2
const UPDATE_FAILED = 3


const getToken = (username, password, environment) => {
    return axios.post(
        HarbourFileImporter.ENV_URLS[environment] + "/api/v1/auth/login",
        {
            "username": username,
            "password": password
        },
        {
            headers: {
                "Content-Type": "application/json"
            }
        }
        )
        .then(
            (response) => {
                console.log("successfully logged in as: " + username)
                console.debug(
                    {
                        "status": response.status,
                        "body": response.data
                    }
                )

                return "Bearer " + response.data
            },
            (err) => {
                console.error(err)
                Promise.reject(err)
            }
        )
}

const loginAndUpdateFile = (username, password, environment, inputFile, endpoint, debug=false) => {
    let importer = new HarbourFileImporter({"debugLog": debug || false})

    getToken(username, password, environment)
        .then(
            (token) => {
                return importer.sendFile(inputFile, endpoint, token, environment)
            },
            (err) => {
                console.error(err)
                process.exit(LOGIN_FAILED)
            })
        .then(
            (res) => {
                console.log(res)
                process.exit(SUCCESS)
            },
            (err) => {
                console.error(err.response.data)
                console.debug(err)
                process.exit(UPDATE_FAILED)
            });
}

const options = yargs
    .usage("Usage: -f <file> -t <type> -e <environment>")
    .option("f", { alias: "file", describe: "Your input file", type: "string", demandOption: true })
    .option("t", { alias: "type", describe: "The type of file you are uploading.", choices:["zones","ratecards"], type: "string", demandOption: true , default: "development"})
    .option("e", { alias: "environment", describe: "The environment to execute against",choices:["development","staging","production"], type: "string", demandOption: true })
    .option("d", { alias: "debug", describe: "Display debug logs", type: "boolean", demandOption: false, default: false })
    .argv;

if (!username || !password) {
    console.error("Missing Credential(s). Please set HARBOUR_USER & HARBOUR_PASSWORD in your environment.\n" +
        "Windows: setx HARBOUR_USER \"Username\", \n" +
        "*nix: export HARBOUR_USER=Username")
    process.exit(MISSING_CREDENTIALS)
}

loginAndUpdateFile(username, password, options.environment, options.file, options.type, options.debug);


