enum DeliveryState {
  PLANNING = "planning",
  PENDING = "pending",
  IN_PROGRESS = "in_progress",
  DELIVERED = "delivered",
  CANCELLED = "cancelled",
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
