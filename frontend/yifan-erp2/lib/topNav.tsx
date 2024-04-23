import {BreadcrumbItem, Breadcrumbs} from "@nextui-org/react";
import { useRouter } from 'next/router';

export default function TopNav(){
  const router = useRouter();
  const currentRoute = router.pathname;

  console.log(currentRoute)
  return (
    <Breadcrumbs>
      <BreadcrumbItem>Home</BreadcrumbItem>
      <BreadcrumbItem>Music</BreadcrumbItem>
      <BreadcrumbItem>Artist</BreadcrumbItem>
      <BreadcrumbItem>Album</BreadcrumbItem>
      <BreadcrumbItem>Song</BreadcrumbItem>
    </Breadcrumbs>
  )
}
