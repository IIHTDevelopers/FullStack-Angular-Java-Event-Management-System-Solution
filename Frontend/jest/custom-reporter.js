const fetch = (url) =>
  import("node-fetch").then(({ default: fetch }) => fetch(url));
let fs = require("fs");
const { request } = require("http");
let xmlBuilder = require("xmlbuilder");
let createXMLFile = false;

let customData = "";

class TestCaseResultDto {
  methodName = "";
  methodType = "";
  actualScore = 0;
  earnedScore = 0;
  status = "";
  isMandatory = true;
  erroMessage = "";
}

class TestResults {
  testCaseResults = "";
  customData = "";
}

// custom-reporter.js
class MyCustomReporter {
  constructor(globalConfig, options) {
    this.xml = xmlBuilder.create("test-cases");

    this._globalConfig = globalConfig;
    this._options = options;

    this.outputFiles = {
      business: "./output_revised.txt",
      boundary: "./output_boundary_revised.txt",
      exception: "./output_exception_revised.txt",
      xml: "./yaksha-test-cases.xml",
    };

    try {
      const data = fs.readFileSync("../../custom.ih", "utf8");
      customData = data;
    } catch (err) {
      console.error(err);
    }

    for (let key in this.outputFiles) {
      if (fs.existsSync(this.outputFiles[key])) {
        fs.unlink(this.outputFiles[key], (err) => {
          if (err) console.log(`${this.outputFiles[key]} not deleted`);
        });
      }
    }

    /*var fr=new window.FileReader();
            fr.onload=function(){
                this.customData = fr.result;
            }
              
           fr.readAsText("../custom.txt");*/
  }

  onRunComplete(contexts, results) {
    // console.log('Custom reporter output:');
    // console.log('GlobalConfig: ', this._globalConfig);
    // console.log('Options: ', this._options);

    // let createXMLFile = false;

    if (createXMLFile) {
      fs.writeFileSync(
        this.outputFiles.xml,
        this.xml.toString({ pretty: true })
      );
    }
  }

  onRunStart() {}
  onTestResult() {
    let results = arguments[1];
    // console.log(results.testResults)

    results.testResults.forEach((result) => {
      writeTextFiles(result, this.outputFiles, function (file, data) {
        fs.appendFileSync(file, data);
      });

      prepareXmlFile(this.xml, result);
    });
  }
}

const writeTextFiles = function (result, outputFiles, cb) {
  //TestResults testResults = new TestResults();
  let test_Results = new TestResults();
  //Map<String, TestCaseResultDto> testCaseResults = new HashMap<String, TestCaseResultDto>();
  //let testCaseResults = new Map();
  let testCaseResults = {};

  let resultStatus = "Failed";
  let resultScore = 0;
  if (result.status === "passed") {
    resultScore = 1;
    resultStatus = "Passed";
  }

  let testName = result.fullName.trim();
  let fileName = testName.split(" ")[1];

  let testNameToCamelCase = camelCase(testName);
  let testCaseResult_Dto = new TestCaseResultDto();
  testCaseResult_Dto.methodName = testNameToCamelCase;
  testCaseResult_Dto.methodType = fileName;
  testCaseResult_Dto.actualScore = 1;
  testCaseResult_Dto.earnedScore = resultScore;
  testCaseResult_Dto.status = resultStatus;
  testCaseResult_Dto.isMandatory = true;
  testCaseResult_Dto.erroMessage = "";

  let GUID = "d907aa7b-3b6d-4940-8d09-28329ccbc070";
  //let customData = "Some Custom Data";
  //testCaseResults.set(this.GUID,JSON.stringify(testCaseResult_Dto));
  testCaseResults[GUID] = testCaseResult_Dto;

  test_Results.testCaseResults = JSON.stringify(testCaseResults);
  test_Results.customData = customData;

  let finalResult = JSON.stringify(test_Results);

  //fs.appendFileSync("./test.txt", finalResult);
  let fileOutput = `${testNameToCamelCase}=${result.status === "passed"}`;
  fs.appendFileSync("./test.txt", finalResult);

  var XMLHttpRequest = require("xhr2");
  var xhr = new XMLHttpRequest();
  var url =
    "https://yaksha-prod-sbfn.azurewebsites.net/api/YakshaMFAEnqueue?code=jSTWTxtQ8kZgQ5FC0oLgoSgZG7UoU9Asnmxgp6hLLvYId/GW9ccoLw==";
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
    fs.appendFileSync(
      "./test.txt",
      "\nRESPONSE" + JSON.stringify(xhr.responseText)
    );
    //var json = JSON.parse(xhr.responseText);
    /*if (xhr.readyState === 4 && xhr.status === 200) {
        fs.appendFileSync("./test.txt", "\nSUCCESS" + json);
          //console.log(json.email + ", " + json.password);
      }
      else{
        fs.appendFileSync("./test.txt", "\nFAILED" + json);
      }*/
  };
  var data = JSON.stringify(test_Results);
  xhr.send(data);

  if (!!outputFiles[fileName]) cb(outputFiles[fileName], `${fileOutput}\n`);
};

const capitalize = function (str) {
  return str.charAt(0).toUpperCase() + str.substring(1).toLowerCase();
};

const camelCase = function (str) {
  var words = str.split(" ").map((word) => {
    return capitalize(word);
  });

  return words.join("").charAt(0).toLowerCase() + words.join("").substring(1);
};

const prepareXmlFile = function (xml, result) {
  // var xml = xmlBuilder.create('test-cases');
  var testCaseType = result.fullName.trim().split(" ")[1];

  xml
    .ele("cases", {
      "xmlns:java": "http://java.sun.com",
      "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
      "xsi:type": "java:com.assessment.data.TestCase",
    })
    .ele(
      "test-case-type",
      capitalize(
        testCaseType == "business"
          ? "functional"
          : testCaseType == "exception"
          ? "exception"
          : "boundary"
      )
    )
    .up()
    .ele("expected-ouput", true)
    .up()
    .ele("name", camelCase(result.fullName.trim()))
    .up()
    .ele("weight", 2)
    .up()
    .ele("mandatory", true)
    .up()
    .ele("desc", "na")
    .end();
};

module.exports = MyCustomReporter;
// or export default MyCustomReporter;
