'use client'
import {useRouter} from "next/navigation";

interface LinkButtonProps {
  urlLink: string;
  text: string;
}

export default function LinkButton(props: LinkButtonProps) {
  let router = useRouter();

  function onClickHandler() {
    router.push(props.urlLink)
  }

  return (
    <button onClick={onClickHandler}
            className={'px-3 py-1.5 m-3 text-white bg-neutral-500 rounded-full shadow-md hover:bg-neutral-700 transition duration-100'}>
      {props.text}</button>
  )
}
