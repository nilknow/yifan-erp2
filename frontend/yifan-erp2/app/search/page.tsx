'use client'
import {useEffect, useState} from "react";
import RouteDto from "@/app/dto/routeDto";
import myFetch from "@/app/myFetch";
import Res from "@/app/dto/res";
import SearchBar from "@/app/lib/searchBar";


export default function Page() {
  const [routes, setRoutes] = useState<RouteDto[]>([])
  useEffect(() => {
    myFetch("/api/route/list")
      .then(res => res.json())
      .then((res: Res<RouteDto[]>) => {
        if (res.successCode === "success") {
          setRoutes(res.body)
        } else {
          alert(res.msg)
        }
      })
  }, [])

  const handleSearch = (value) => {
    console.log("Search term:", value);
    // Perform your search logic here using the value
  };

  return (
    <div
      className="w-screen h-screen px-8 flex justify-center items-center bg-gradient-to-tr from-pink-500 to-yellow-500 text-white shadow-lg">
      <SearchBar onSearch={handleSearch}/>
      <ul>
        {
          routes.map((route: RouteDto) => (
            <li key={route.path}>{route.name}</li>
          ))
        }
      </ul>
    </div>
  )
}
