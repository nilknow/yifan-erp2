'use client'
import {useRef, useState} from "react";
import {CloseFilledIcon} from "@nextui-org/shared-icons";
import Image from "next/image";

interface SearchBarProps {
  onSearch: (value: string) => void; // Define the prop type for onSearch
}

export default function SearchBar({ onSearch }:SearchBarProps) {
  const [inputValue, setInputValue] = useState<string>("");
  const inputRef = useRef(null);

  const handleInputChange = (event: any) => {
    setInputValue(event.target.value);
    onSearch(inputValue);
  };

  function clearInputHandler() {
    setInputValue("");
  }

  function handleSearchClick(){
    inputRef.current?.focus();
  }

  return (
    <>
      <div
        onClick={handleSearchClick}
        className={" flex shadow-md fixed top-1/3 left-1/2 -translate-x-1/2 -translate-y-1/2 " +
        " px-3 rounded-2xl bg-white/25 drop-shadow-lg text-black backdrop-blur-xl " +
        " hover:cursor-text hover:bg-gray-200 focus:outline-none "}>
        <Image
          alt="Search Icon"
          src={"/search.svg"}
          width={24}
          height={24}/>
        <input
          ref={inputRef}
          value={inputValue}
          onChange={handleInputChange}
          className={"w-[550px] bg-transparent placeholder-gray-400 hover:placeholder-gray-400 text-md focus:outline-none pl-1 py-2.5 "}
          placeholder={"请输入您想跳转的页面..."}/>
        <div className={"w-4 flex items-center justify-center"}>
          {inputValue.length > 0 && (
            <button
              onClick={clearInputHandler}
              type="button"
              className="focus:outline-none"
            >
              <CloseFilledIcon color="black"/>
            </button>
          )}
        </div>
      </div>
    </>
  )
}
