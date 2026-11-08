/**
 * Returns dynamic CSS classes for risk badges and indicators based on risk level
 */
export function getRiskBadgeClass(level?: string): string {
  const normalized = level?.toLowerCase() || '';

  if (normalized === 'high') {
    return 'bg-[#ff6978]/20 text-[#ff6978] border border-[#ff6978]/30';
  }
  if (normalized === 'medium') {
    return 'bg-[#f0ad55]/20 text-[#f0ad55] border border-[#f0ad55]/30';
  }
  return 'bg-[#43d6a0]/20 text-[#43d6a0] border border-[#43d6a0]/30';
}

/**
 * Returns dynamic dot color for alerts or status indicators
 */
export function getRiskDotClass(severity?: string): string {
  const normalized = severity?.toLowerCase() || '';

  if (normalized === 'high') {
    return 'bg-[#ff6978]';
  }
  if (normalized === 'medium') {
    return 'bg-[#f0ad55]';
  }
  return 'bg-[#43d6a0]';
}

/**
 * Checks if a change string represents a positive trend (starts with '+')
 */
export function isPositiveChange(change?: string): boolean {
  if (!change) return false;
  return change.startsWith('+');
}

/**
 * Returns the appropriate text color class based on the trend sign (+/-)
 */
export function getTrendColorClass(change?: string): string {
  return isPositiveChange(change) ? 'text-emerald-400' : 'text-rose-400';
}

/**
 * Returns dynamic CSS classes for action status badges
 */
export function getActionStatusClass(status?: string): string {
  const normalized = status?.toLowerCase() || '';

  if (normalized === 'pending') {
    return 'bg-[#f0ad55]/20 text-[#f0ad55] border border-[#f0ad55]/30';
  }
  if (normalized === 'applied') {
    return 'bg-[#43d6a0]/20 text-[#43d6a0] border border-[#43d6a0]/30';
  }
  if (normalized === 'in review') {
    return 'bg-[#4c8dff]/20 text-[#4c8dff] border border-[#4c8dff]/30';
  }
  return 'bg-[#222830] text-[#8994a2] border border-[#2b333d]';
}

/**
 * Generates an SVG polyline points string for line charts based on data array
 */
export function generateLinePath(data?: number[]): string {
  if (!data || data.length === 0) return '';
  const max = Math.max(...data);
  const min = Math.min(...data);
  const range = max - min || 1;
  const width = 200;
  const height = 40;

  return data
    .map((val, idx) => {
      const x = (idx / (data.length - 1)) * width;
      const y = height - ((val - min) / range) * height;
      return `${x},${y}`;
    })
    .join(' ');
}


/**
 * Calculates stroke-dashoffset for circular risk score gauge
 */
export function calculateCircleOffset(scoreValue: string): number {
  const numericVal = parseInt(scoreValue, 10) || 50;
  const circumference = 2 * Math.PI * 15; // r = 15
  return circumference - (numericVal / 100) * circumference;
}

/**
 * Calculates cumulative stroke-dashoffset for SVG donut/allocation slices
 */
export function getCumulativeOffset(items: Array<{ percentage: number }>, index: number): number {
  let offset = 0;
  for (let i = 0; i < index; i++) {
    offset += items[i].percentage;
  }
  return -offset;
}

/**
 * Returns a consistent dynamic color class for portfolio badge codes based on a string hash
 */
export function getPortfolioBadgeColor(code?: string): string {
  if (!code) return 'text-[#4c8dff]';
  const upper = code.toUpperCase();

  const colorPalette = [
    'text-[#4386f5]',
    'text-[#a855f7]',
    'text-[#38bdf8]',
    'text-[#fb923c]',
    'text-[#34d399]',
    'text-[#f43f5e]',
    'text-[#fbbf24]',
    'text-[#a78bfa]',
  ];

  let hash = 0;
  for (let i = 0; i < upper.length; i++) {
    hash = upper.charCodeAt(i) + ((hash << 5) - hash);
  }
  return colorPalette[Math.abs(hash) % colorPalette.length];
}
