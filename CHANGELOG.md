# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)

## [Unreleased]
### Added
### Changed
### Removed
### Fixed
### Security

## [0.3.0] 2020-08-23  (Unreleased to production)
### Added
- INFRA | Base AWS VPC Environment
- INFRA | Created proper health endpoint that calls DB
- INFRA | Staging App and API deployed to https://uat.staging.freightmate,com & https://api.staging.freightmate.com respectively
- INFRA | CI/CD pipeline to auto deploy to staging environment
- FRONTEND | Select and create delivery address on the create consignment page
- FRONTEND | Consignment items UI
- BACKEND | Address Search functionality
- BACKEND | Consignment item CRUD
- BACKEND | Auto populate created_by, updated_by and deleted_by ids and creating a unified BaseEntity POJO
### Changed
- BACKEND | Consignment CRUD to allow brokers & customers to create a consignment for a client
- BACKEND | JWT content now includes id, along with preferred units. 
- BACKEND | Authorisation principal is now an AuthToken POJO instead of a username
### Removed
### Fixed
- BACKEND | Existing CRUD Authorization (Broker,Customer editing clients)
- BACKEND | Auspost API bug for single Locality response
- BACKEND | Create Consignment endpoint gracefully response to empty request
- FRONTEND | Inconsistent Button text for confirm modal
- FRONTEND | Display username correctly when changing accounts  
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

[Unreleased]: https://github.com/Freightmate/harbour/compare/0.3.0...HEAD
[0.2.0]: https://github.com/Freightmate/harbour/compare/0.0.0...0.2.0
[0.2.1]: https://github.com/Freightmate/harbour/compare/0.2.0...0.2.1
[0.3.0]: https://github.com/Freightmate/harbour/compare/0.2.1...0.3.0
