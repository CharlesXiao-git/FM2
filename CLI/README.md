## Harbour CLI
A simple command line tool to interact with the Harbour web application

#### Install
To install you can follow either approach below, local install is better for development, and global for each release.  
The application also expects two environment variables (`HARBOUR_PASSWORD`, `HARBOUR_USER`) to be set to log in to the web application. 
```shell script
 # local install
npm install

# Global install (call harbourImport anywhere on cli)
cpm install -g .
```
#### Usage
```shell script
Usage: -f <file> -t <type> -e <environment>

Options:
  --help             Show help                                         [boolean]
  --version          Show version number                               [boolean]
  -f, --file         Your input file                         [string] [required]
  -t, --type         The type of file you are uploading.
                             [string] [required] [choices: "zones", "ratecards"]
  -e, --environment  The environment to execute against
           [string] [required] [choices: "development", "staging", "production"]
  -d, --debug        Display debug logs               [boolean] [default: false]

Missing required arguments:
   f, t, e
```
#### Example use
```shell script
# local installed
node .  -f ./myfile.csv -t ratecards -e development

# Globally installed
harbourImport  -f ./myfile.csv -t ratecards -e development

# Globally installed with debug logs enabled
harbourImport  -f ./myfile.csv -t ratecards -e development -d true
```

#### Maintenance
Executables are defined in `package.json`  
The package [yargs](https://yargs.js.org/docs/) has been used for CLI functionality.  

