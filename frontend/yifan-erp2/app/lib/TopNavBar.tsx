'use client'

import {useRouter} from "next/navigation";
import {
  BreadcrumbItem,
  Breadcrumbs,
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Image
} from "@nextui-org/react";
import myFetch from "@/app/myFetch";
import {ChevronDownIcon} from "@nextui-org/shared-icons";

export default function TopNavBar() {
  let router = useRouter();

  //fix it
  async function logoutHandler(e: any) {
    e.preventDefault()
    await myFetch('/api/logout', {
      method: 'POST',
    }).then(() => {
      router.push('/')
    })
    return;
  }

  async function routeTree() {
    const data = await myFetch('/api/route/tree', {
      method: 'GET',
    })
    return data;
  }

  return (
    <div className={"flex items-center"}>
      <a href={"/"}>
        <img src={"/66_long.png"} className={"w-8 h-8 m-5 inline"} loading={"eager"}/>
      </a>
      <a href={"javascript:history.back()"}>
        <img src={"/back.png"} className={"w-8 h-8 m-5 inline"} loading={"eager"}/>
      </a>
      {/*<a href="/"*/}
      {/*   className={"text-neutral-500 hover:bg-neutral-700 hover:text-white p-3 text-left font-bold transition duration-100"}*/}
      {/*>返回首页</a>*/}
      {/*<Breadcrumbs*/}
      {/*  size={"lg"}*/}
      {/*  className={"m-4"}*/}
      {/*  itemClasses={{*/}
      {/*    item: "px-2",*/}
      {/*    separator: "px-0",*/}
      {/*  }}*/}
      {/*>*/}
      {/*  <BreadcrumbItem href="/">*/}
      {/*    <Dropdown>*/}
      {/*      <DropdownTrigger>*/}
      {/*        <Button*/}
      {/*          className="h-6 pr-2 text-small"*/}
      {/*          endContent={<ChevronDownIcon className="text-default-500"/>}*/}
      {/*          radius="full"*/}
      {/*          size="sm"*/}
      {/*          variant="light"*/}
      {/*        >*/}
      {/*          首页*/}
      {/*        </Button>*/}
      {/*      </DropdownTrigger>*/}
      {/*      <DropdownMenu aria-label="Routes">*/}
      {/*        <DropdownItem href="/admin">*/}
      {/*          系统管理*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song2">*/}
      {/*          Song 2*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song3">*/}
      {/*          Song 3*/}
      {/*        </DropdownItem>*/}
      {/*      </DropdownMenu>*/}
      {/*    </Dropdown>*/}

      {/*  </BreadcrumbItem>*/}
      {/*  <BreadcrumbItem href="#music">Music</BreadcrumbItem>*/}
      {/*  <BreadcrumbItem href="#artist">*/}
      {/*    <Dropdown>*/}
      {/*      <DropdownTrigger>*/}
      {/*        <Button*/}
      {/*          className="h-6 pr-2 text-small"*/}
      {/*          endContent={<ChevronDownIcon className="text-default-500"/>}*/}
      {/*          radius="full"*/}
      {/*          size="sm"*/}
      {/*          variant="light"*/}
      {/*        >*/}
      {/*          Artists*/}
      {/*        </Button>*/}
      {/*      </DropdownTrigger>*/}
      {/*      <DropdownMenu aria-label="Routes">*/}
      {/*        <DropdownItem href="#song-1">*/}
      {/*          Song 1*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song2">*/}
      {/*          Song 2*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song3">*/}
      {/*          Song 3*/}
      {/*        </DropdownItem>*/}
      {/*      </DropdownMenu>*/}
      {/*    </Dropdown>*/}
      {/*  </BreadcrumbItem>*/}
      {/*  <BreadcrumbItem href="#album">*/}
      {/*    <Dropdown>*/}
      {/*      <DropdownTrigger>*/}
      {/*        <Button*/}
      {/*          className="h-6 pr-2 text-small"*/}
      {/*          endContent={<ChevronDownIcon className="text-default-500"/>}*/}
      {/*          radius="full"*/}
      {/*          size="sm"*/}
      {/*          variant="light"*/}
      {/*        >*/}
      {/*          Albums*/}
      {/*        </Button>*/}
      {/*      </DropdownTrigger>*/}
      {/*      <DropdownMenu aria-label="Routes">*/}
      {/*        <DropdownItem href="#song-1">*/}
      {/*          Song 1*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song2">*/}
      {/*          Song 2*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song3">*/}
      {/*          Song 3*/}
      {/*        </DropdownItem>*/}
      {/*      </DropdownMenu>*/}
      {/*    </Dropdown>*/}
      {/*  </BreadcrumbItem>*/}
      {/*  <BreadcrumbItem*/}
      {/*    classNames={{*/}
      {/*      item: "px-0",*/}
      {/*    }}*/}
      {/*  >*/}
      {/*    <Dropdown>*/}
      {/*      <DropdownTrigger>*/}
      {/*        <Button*/}
      {/*          className="h-6 pr-2 text-small"*/}
      {/*          endContent={<ChevronDownIcon className="text-default-500"/>}*/}
      {/*          radius="full"*/}
      {/*          size="sm"*/}
      {/*          variant="light"*/}
      {/*        >*/}
      {/*          Songs*/}
      {/*        </Button>*/}
      {/*      </DropdownTrigger>*/}
      {/*      <DropdownMenu aria-label="Routes">*/}
      {/*        <DropdownItem href="#song-1">*/}
      {/*          Song 1*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song2">*/}
      {/*          Song 2*/}
      {/*        </DropdownItem>*/}
      {/*        <DropdownItem href="#song3">*/}
      {/*          Song 3*/}
      {/*        </DropdownItem>*/}
      {/*      </DropdownMenu>*/}
      {/*    </Dropdown>*/}
      {/*  </BreadcrumbItem>*/}
      {/*</Breadcrumbs>*/}

      {/*<a href="/logout"*/}
      {/*   onClick={logoutHandler}*/}
      {/*   className={"text-neutral-500 hover:bg-neutral-700 hover:text-white p-3 text-left font-bold transition duration-100"}*/}
      {/*>登出</a>*/}
    </div>
  )
}
