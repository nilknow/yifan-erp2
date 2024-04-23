import Material from "@/lib/dto/material";
import Category from "@/lib/dto/category";

export default interface Product {
  id: string,
  serialNum: string,
  name: string,
  materials: Material[],
  category: Category,
  description: string,
  unit: string,
  count: number,
}
