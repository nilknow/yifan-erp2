interface NavBoxProps {
  title: string;
  links: { label: string; path: string }[];
}

export default function NavBox(props: NavBoxProps) {
  return (
    <div className={"border border-gray-300 rounded-lg p-4 m-4 bg-black"}>
      <h3 className={"m-3 text-3xl"}>{props.title}</h3>
      <ul className={"m-4"}>
        {props.links.map((link, index) => (
          <li key={index}>
            <a className={"text-blue-500 hover:text-blue-700 hover:underline"} href={link.path}>{link.label}</a>
          </li>
        ))}
      </ul>
    </div>
  )
}
