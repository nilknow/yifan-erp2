import Material from "@/lib/dto/material";
import Product from "@/lib/dto/product";

export default interface ProductMaterialRel {
  id: number;
  product: Product;
  material: Material;
  materialCount: number
}
