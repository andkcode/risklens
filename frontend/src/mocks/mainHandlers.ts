import { authHandlers } from './authHandlers';
import { dashboardHandlers } from './dashboardHandlers';
import { portfolioHandlers} from './portfolioHandlers';

export const handlers = [
  ...authHandlers,
  ...dashboardHandlers,
  ...portfolioHandlers,
];
