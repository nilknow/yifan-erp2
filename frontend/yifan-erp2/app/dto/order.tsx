import Product from "@/app/dto/product";
import Delivery from "@/app/dto/delivery";

export default interface Order{
  id: number,
  serialNum: string,
  product: Product,
  count: number
  produceDaysTake:number
  delivery:Delivery
  customer:string
  orderSerialNum:string
  note:string
  createTime:Date
  updateTime:Date
}
