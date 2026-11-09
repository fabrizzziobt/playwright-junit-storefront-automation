# playwright-storefront-automation

Locale-aware MVP storefront automation template using Playwright with Java and Maven.

## What changed in this version
- Full PDP URLs were removed from product fixtures.
- Locale-specific product files were introduced.
- Locale now determines:
  - base domain,
  - browser locale,
  - product file.

## Why this is better
- Cleaner product fixtures.
- Easier support for regional catalogs.
- Less duplication across environments.
- Safer test execution when switching locales.

## Structure
- `config/test.properties`: locale mappings and runtime defaults.
- `data/products-*.json`: locale-specific product catalogs.
- `config/TestEnvironment.java`: resolved runtime environment.
- `utils/LocaleResolver.java`: locale lookup helper.
- `pages`: page objects.
- `tests`: business scenarios.

## Run examples
```bash
mvn test
mvn test -Dlocale=en_CA
mvn test -Dlocale=en_GB -Dbrowser=firefox
mvn test -Dlocale=en_US -Dheadless=false
