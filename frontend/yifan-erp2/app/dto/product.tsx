import Material from "@/app/dto/material";
import Category from "@/app/dto/category";

export default interface Product {
  id: string,
  serialNum:string,
  name: string,
  materials: Material[],
  category: Category,
  description: string,
  unit: string,
}
