// userList.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function UserList() {

    const navigate = useNavigate();
	const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);
    const [maxPage, setMaxPage] = useState();
    const [pageNum, setPageNum] = useState(0);
    const [pageSize, setPageSize] = useState(2);

	useEffect(() => {
        const reqBody = {
            "pageNo" : pageNum,
            "pageSize" : pageSize,
            "sort" : "ASC",
            "sortByColumn" : "id"
        };
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer "  + localStorage.getItem("jwt"),
			},
			method: "post",
            body: JSON.stringify(reqBody)
		};
		fetch("https://localhost:8080/admin/user/list", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            console.log(response);
            setUsers(response.Users.content);
            setMaxPage(response.Users.totalPages);
            setLoading(false);
        })
        .catch((message) => {
            console.log(message);
            navigate("/unauthorized/user");
            
        });
	}, [pageNum, pageSize, navigate]);

    const nextPage = (e) => {
        if (pageNum + 1 < maxPage){
            setPageNum(pageNum + 1);
            const reqBody = {
                "pageNo" : pageNum,
                "pageSize" : pageSize,
                "sort" : "ASC",
                "sortByColumn" : "id"
            };
            const fetchOptions = {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer "  + localStorage.getItem("jwt"),
                },
                method: "post",
                body: JSON.stringify(reqBody)
            };
            fetch("https://localhost:8080/admin/user/list", fetchOptions)
            .then((response) => response.json())
            .then((response) => {
                setUsers(response.Users.content);
                setLoading(false);
            })
            .catch((message) => console.log(message));
        }
    };

    const previousPage = (e) => {
        if(pageNum > 0) {
            setPageNum(pageNum - 1);
            const reqBody = {
                "pageNo" : pageNum,
                "pageSize" : pageSize,
                "sort" : "ASC",
                "sortByColumn" : "id"
            };
            const fetchOptions = {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer "  + localStorage.getItem("jwt"),
                },
                method: "post",
                body: JSON.stringify(reqBody)
            };
            fetch("https://localhost:8080/admin/user/list", fetchOptions)
            .then((response) => response.json())
            .then((response) => {
                setUsers(response.Users.content);
                setLoading(false);
            })
            .catch((message) => console.log(message));
        }
    };

    const firstPage = (e) => {
        setPageNum(0);
        const reqBody = {
            "pageNo" : pageNum,
            "pageSize" : pageSize,
            "sort" : "ASC",
            "sortByColumn" : "id"
        };
        const fetchOptions = {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer "  + localStorage.getItem("jwt"),
            },
            method: "post",
            body: JSON.stringify(reqBody)
        };
        fetch("https://localhost:8080/admin/user/list", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setUsers(response.Users.content);
            setLoading(false);
        })
        .catch((message) => console.log(message));
    };

    const lastPage = (e) => {
        setPageNum(maxPage - 1);
        const reqBody = {
            "pageNo" : pageNum,
            "pageSize" : pageSize,
            "sort" : "ASC",
            "sortByColumn" : "id"
        };
        const fetchOptions = {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer "  + localStorage.getItem("jwt"),
            },
            method: "post",
            body: JSON.stringify(reqBody)
        };
        fetch("https://localhost:8080/admin/user/list", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            console.log(response);
            setUsers(response.Users.content);
            setLoading(false);
        })
        .catch((message) => console.log(message));
    };

    const changePageSize = (newPageSize) => {
        setPageSize(newPageSize);
        const reqBody = {
            "pageNo" : pageNum,
            "pageSize" : pageSize,
            "sort" : "ASC",
            "sortByColumn" : "id"
        };
        const fetchOptions = {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer "  + localStorage.getItem("jwt"),
            },
            method: "post",
            body: JSON.stringify(reqBody)
        };
        fetch("https://localhost:8080/admin/user/list", fetchOptions)
        .then((response) => response.json())
        .then((response) => {
            setUsers(response.Users.content);
            setLoading(false);
        })
        .catch((message) => console.log(message));
    };

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<h1> User list : </h1>
            <div>
                <ul>
                    {users.map((data) => (
                    <li key={data.id}>
                        <p><a href={"https://localhost:3000/admin/user/" + data.id + "/details"}>{data.username}</a></p>
                    </li>
                ))}
                </ul>
            </div>
            <div>
                Current page : {pageNum + 1}
            </div>
            <div>
                <button id="submit" type="button" onClick={() => nextPage()}>
                        Next Page
                </button>
            </div>
            <div>
                <button id="submit" type="button" onClick={() => previousPage()}>
                        Previous Page
                </button>
            </div>
            <div>
                {pageNum > 0 && 
                    <>
                        <button id="submit" type="button" onClick={() => firstPage()}>
                                First Page
                        </button>
                    </>
                }
            </div>
            <div>
                {pageNum < maxPage - 1 && 
                    <>
                        <button id="submit" type="button" onClick={() => lastPage()}>
                                Last Page
                        </button>
                    </>
                }
            </div>
            <div>
                <label for="numPages" >
					Users per page : 
                </label>
                <select name="numPages" id="pageSize" value={pageSize} onChange={(event) => changePageSize(event.target.value)}>
                    <option value= {1}> 1 </option>
                    <option value={2}> 2 </option>
                    <option value={5}> 5 </option>
                    <option value={10}> 10 </option>
                </select>
            </div>
		</>
	);
}

export default UserList;