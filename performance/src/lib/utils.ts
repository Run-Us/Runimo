export function randomIntBetween(min: number, max: number): number {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

export function randomFloatBetween(min: number, max: number): number {
  return Math.random() * (max - min) + min;
}

export function formatDate(date: Date): string {
  return date.toISOString().slice(0, 10);
}

export function getRandomDateInPast(daysAgo: number = 30): Date {
  const date = new Date();
  date.setDate(date.getDate() - randomIntBetween(0, daysAgo));
  return date;
}

export function generateUserIds(count: number): number[] {
  return Array.from({ length: count }, (_, i) => i + 1);
}

export function buildQueryString(params: Record<string, any>): string {
  const queryString = Object.entries(params)
    .filter(([_, value]) => value !== undefined && value !== null)
    .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
    .join('&');
  
  return queryString ? `?${queryString}` : '';
}
