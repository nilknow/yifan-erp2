export default interface ActionLogDto {
  id: number;
  batchId: string;
  eventType: string;
  timestamp: Date;
  username: string;
  additionalInfo: string;
  description: string;
}
