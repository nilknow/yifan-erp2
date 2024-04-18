'use client'
import {Table, TableBody, TableCell, TableColumn, TableHeader, TableRow} from "@nextui-org/react";
import ActionLogDto from "@/app/dto/actionLogDto";
import {useEffect, useState} from "react";
import myFetch from "@/app/myFetch";
import Res from "@/app/dto/res";

export default function Page() {
  const [actionLogDtos, setActionLogDtos] = useState<ActionLogDto[]>([]);
  useEffect(() => {
    myFetch("/api/order/action_log/list")
      .then(res => res.json())
      .then((data: Res<ActionLogDto[]>) => {
        if (data.successCode === 'success') {
          setActionLogDtos(data.body)
        } else {
          alert(data.msg)
        }
      })
  },[])

  return (
    <div>
      <Table>
        <TableHeader aria-label={"History Info Table"}>
          <TableColumn>Event Time</TableColumn>
          <TableColumn>Event type</TableColumn>
          <TableColumn>UserId</TableColumn>
          <TableColumn>Description</TableColumn>
          <TableColumn>Additional Info</TableColumn>
        </TableHeader>
        <TableBody>
          {
            actionLogDtos.map((item: ActionLogDto) => (
              <TableRow key={item.id}>
                <TableCell>{new Date(item.timestamp).toLocaleString()}</TableCell>
                <TableCell>{item.eventType}</TableCell>
                <TableCell>{item.username}</TableCell>
                <TableCell>{item.description}</TableCell>
                <TableCell>{item.additionalInfo}</TableCell>
              </TableRow>
            ))
          }
        </TableBody>
      </Table>
    </div>
  )
}
