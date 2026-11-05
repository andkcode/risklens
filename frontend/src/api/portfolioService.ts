import { axiosInstance } from './client';

export interface PortfolioResponseDto {
  id: string;
  name: string;
  baseCurrency: string;
  createdAt: string;
  updatedAt: string;
}

interface PageResponse<T> {
  content: T[];
}

export const portfolioService = {
  async getPortfolios(page = 0, size = 10): Promise<PageResponse<PortfolioResponseDto>> {
    const response = await axiosInstance.get<PageResponse<PortfolioResponseDto>>(
      '/api/v1/portfolios',
      { params: { page, size } },
    );
    return response.data;
  },
};
