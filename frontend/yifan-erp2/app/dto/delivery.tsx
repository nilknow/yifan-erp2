export enum DeliveryState {
  PLANNING = "PLANNING",
  PENDING = "PENDING",
  IN_PROGRESS = "IN_PROGRESS",
  DELIVERED = "DELIVERED",
  CANCELLED = "CANCELLED",
}

export default interface Delivery {
  id: number;
  serialNum?: string;
  state:DeliveryState;
  price: number;
  note?: string;
  planDate:Date;
  createDate: Date;
  updateDate: Date;
}
