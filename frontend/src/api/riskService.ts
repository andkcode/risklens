import { axiosInstance } from './client';

export interface RiskAssessmentResponseDto {
  id: string;
  portfolioId: string;
  overallVaR: number;
  activeRiskScore: number;
  createdAt: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export const riskService = {
  // Fetch paginated risk assessments for a specific portfolio
  async getRiskAssessments(portfolioId: string, page = 0, size = 10): Promise<PageResponse<RiskAssessmentResponseDto>> {
    const response = await axiosInstance.get<PageResponse<RiskAssessmentResponseDto>>(
      `/api/v1/portfolios/${portfolioId}/risk-assessments`,
      { params: { page, size } }
    );
    return response.data;
  },

  // Trigger a new risk calculation
  async calculateRisk(portfolioId: string, riskMethod?: string): Promise<RiskAssessmentResponseDto> {
    const response = await axiosInstance.post<RiskAssessmentResponseDto>('/api/v1/risk-assessments', {
      portfolioId,
      riskMethod,
    });
    return response.data;
  },
};