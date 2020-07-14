
## [0.0.4] - 2014-08-09
### Added
- Better explanation of the difference between the file ("CHANGELOG")
and its function "the change log".

### Changed
- Refer to a "change log" instead of a "CHANGELOG" throughout the site
to differentiate between the file and the purpose of the file — the
logging of changes.

### Removed
- Remove empty sections from CHANGELOG, they occupy too much space and
create too much noise in the file. People will have to assume that the
missing sections were intentionally left out because they contained no
notable changes.

See https://keepachangelog.com/en/1.0.0/ for more info

## [0.0.0] - 2020-06-30 UNRELEASED
### Added
- CORE | Core repo generated including frontend and backend sub-projects
- CORE | CHANGELOG and README  
- FRONTEND | VueJS Typescript project using [vue-cli](https://cli.vuejs.org/), including plugins `babel`,`typescript`,`router`,`eslint`,`unit-jest`  
- BACKEND | Spring boot Java 11 project, including plugins related to DB connections, security and `SpringMVC`
- BACKEND | Begin to implement authentication through spring
- BACKEND | Add to authentication endpoints, change login url, add process to undo double hashing, tweak docker-compose local conf
- BACKEND | Add Authorisation to application endpoints
- BACKEND | Changed config to be env var driven