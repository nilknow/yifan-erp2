import Company from "@/app/dto/company";
import Authority from "@/app/dto/authority";

export default interface LoginUser{
  id: number;
  username: string;
  authorities: Authority[];
  company: Company;
}