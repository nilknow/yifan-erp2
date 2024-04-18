export default interface CreateOrderDto{
  id: number,
  serialNum: string,
  productId: number,
  count: number
  produceDaysTake:number
  deliveryId:number
  customer:string
  note:string
}
