export default interface ActionLog {
  id: number;
  batchId: string;
  eventType: string;
  timestamp: Date;
  userId: number;
  tableName: string;
  source: string;
  additionalInfo: string;
  description: string;
}
