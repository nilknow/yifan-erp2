import Company from "@/app/dto/company";
import Authority from "@/app/dto/authority";
import RouteDto from "@/app/dto/routeDto";

export default interface LoginUser{
  id: number;
  username: string;
  authorities: Authority[];
  routes:RouteDto[];
  company: Company;
}