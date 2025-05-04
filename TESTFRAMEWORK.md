# Test Framework Overview

## Test Code Package Structure
- **com.springboot.apitests** : Test are written inside this package. Can find CartOfferApplicationTests test class inside this package.
- **com.springboot.teststeps** : To keep the tests cleaner and easy to maintain, test class contains only high level flow and assertions. Internal logics for different test steps are present in "com.springboot.teststeps" package.
- **com.springboot.utils** : Contains utility classes used by test framework. 
  - *APIClient* class abstracts the code to run REST API methods (GET and POST methods are used for now)
  - *TestDataUtil* class abstracts the code to parse and retrieve test data based on test case ID from test data json file.

## Test Data Management:
- Test data is stored in a JSON file **resources/testdata/ApplyCartOfferData.json**
- TestDataUtil is written in a way it makes framework scale for other test features

## Test Result Report:
- Extent report is integrated in the framework for a clean html report.
- Test report will be added/replaced at **test-output/ApplyCartOfferTestReport.html** after every test run.
- A sample test report from the local run is already added to the repo.

## Scope of improvement:
- As apply offer to cart feature depends on add offer and get user segment APIs, the remaining tests shouldn't trigger if the sanity tests (**sanityTest_AddOfferAPI** or **sanityTest_GetUserSegmentAPI**) fail.
- Reporting can be enhanced to add requests and responses if test fails. Will make it easier and quicker to analyse the failure.
