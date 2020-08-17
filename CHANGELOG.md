# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)

## [Unreleased]
### Added
- INFRA | Base AWS VPC Environment
### Changed
### Removed
### Fixed
### Security

## [0.2.1] - 2020-08-04 (Unreleased to production)
### Fixed
- BACKEND | Bug fix for delete and update in consignment controller
- BACKEND | Allows clients to delete and update consignments and correctly pass through DTOs

## [0.2.0] - 2020-08-03 (Unreleased to production)
### Added
- FRONTEND | User login
- FRONTEND | Home page
- FRONTEND | Alert Component and Confirm action modal
- FRONTEND | Address Book Components
- BACKEND | Address Model and controller
- BACKEND | Begin to implement authentication through spring
- BACKEND | Add to authentication endpoints, change login url, add process to undo double hashing, tweak docker-compose local conf
- BACKEND | Add Authorisation to application endpoints
- BACKEND | Integrate Auspost locality REST API and add postcode lookup to the Address Controller
- BACKEND | Configure Default HTTP REST client settings

### Changed
- BACKEND | Changed config to be env var driven

## [0.0.0] - 2020-06-30 (Unreleased to production)
### Added
- CORE | Core repo generated including frontend and backend sub-projects
- CORE | CHANGELOG and README  
- FRONTEND | VueJS Typescript project using [vue-cli](https://cli.vuejs.org/), including plugins `babel`,`typescript`,`router`,`eslint`,`unit-jest`  
- BACKEND | Spring boot Java 11 project, including plugins related to DB connections, security and `SpringMVC`

`Beginning of changelog`

[Unreleased]: https://github.com/Freightmate/harbour/compare/0.2.1...HEAD
[0.2.0]: https://github.com/Freightmate/harbour/compare/0.0.0...0.2.0
[0.2.1]: https://github.com/Freightmate/harbour/compare/0.2.0...0.2.1