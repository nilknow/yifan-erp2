import Material from "@/app/dto/material";
import Product from "@/app/dto/product";

export default interface ProductMaterialRel {
  id: number;
  product: Product;
  material: Material;
  materialCount: number
}
