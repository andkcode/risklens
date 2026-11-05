// frontend/src/utils/errorMapper.ts
import { AxiosError } from 'axios';

// RFC 7807 ProblemDetail structure with potential validation extensions
export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  // Custom map for field validation errors (e.g., { email: "Invalid format", password: "Too short" })
  fieldErrors?: Record<string, string>;
  // Standard RFC 7807 invalid params extension
  invalidParams?: Array<{ name: string; reason: string }>;
}

export interface ParsedApiError {
  generalMessage: string;
  fieldErrors: Record<string, string>;
}

/**
 * Parses an Axios error containing a ProblemDetail body into clean field and general messages.
 */
export function parseProblemDetail(error: unknown): ParsedApiError {
  const result: ParsedApiError = {
    generalMessage: 'An unexpected error occurred. Please try again.',
    fieldErrors: {},
  };

  if (error instanceof AxiosError && error.response?.data) {
    const data = error.response.data as ProblemDetail;

    // Extract general error description
    if (data.detail) {
      result.generalMessage = data.detail;
    } else if (data.title) {
      result.generalMessage = data.title;
    }

    // Map fieldErrors dictionary if provided by backend
    if (data.fieldErrors && typeof data.fieldErrors === 'object') {
      result.fieldErrors = { ...data.fieldErrors };
    }

    // Map RFC 7807 invalidParams array if provided
    if (Array.isArray(data.invalidParams)) {
      data.invalidParams.forEach(param => {
        if (param.name && param.reason) {
          result.fieldErrors[param.name] = param.reason;
        }
      });
    }
  }

  return result;
}