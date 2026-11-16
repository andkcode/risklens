# DefaultApi

All URIs are relative to *http://localhost*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**apiV1RiskAssessmentsPost**](#apiv1riskassessmentspost) | **POST** /api/v1/risk-assessments | Calculate risk assessment|

# **apiV1RiskAssessmentsPost**
> apiV1RiskAssessmentsPost(riskAssessmentCreateRequestDto)


### Example

```typescript
import {
    DefaultApi,
    Configuration,
    RiskAssessmentCreateRequestDto
} from './api';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

let riskAssessmentCreateRequestDto: RiskAssessmentCreateRequestDto; //

const { status, data } = await apiInstance.apiV1RiskAssessmentsPost(
    riskAssessmentCreateRequestDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **riskAssessmentCreateRequestDto** | **RiskAssessmentCreateRequestDto**|  | |


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | Created |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

