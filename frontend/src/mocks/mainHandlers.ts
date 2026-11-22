import { authHandlers } from './authHandlers';
import { dashboardHandlers } from './dashboardHandlers';
import { portfolioHandlers} from './portfolioHandlers';
import {riskHandlers} from "./riskHandlers";

export const handlers = [
  ...authHandlers,
  ...dashboardHandlers,
  ...portfolioHandlers,
  ...riskHandlers,
];
