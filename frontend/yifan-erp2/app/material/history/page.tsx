'use client'
import Res from "@/app/dto/res";
import {useEffect, useState} from "react";
import {Table, TableBody, TableCell, TableColumn, TableHeader, TableRow} from "@nextui-org/react";
import ActionLogDto from "@/app/dto/actionLogDto";

export default function Page() {
  const [actionLogDtos, setActionLogDtos] = useState<ActionLogDto[]>([]);
  useEffect(() => {
    fetch("/api/material/action_log/list")
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
                <TableCell>{new Date(item.timestamp).toLocaleTimeString()}</TableCell>
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
