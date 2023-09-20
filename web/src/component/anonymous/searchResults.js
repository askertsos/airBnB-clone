// SearchResults.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function SearchResults() {

    const navigate = useNavigate();
	const [loading, setLoading] = useState(true);
    const [rentals, setRentals] = useState([]);
    const [maxPage, setMaxPage] = useState();
    const [pageNum, setPageNum] = useState(0);

    const dates = localStorage.getItem("search_dates");
    const country = localStorage.getItem("search_country");
    const city = localStorage.getItem("search_city");
    const neighbourhood = localStorage.getItem("search_neighbourhood");

	useEffect(() => {
		const reqBody = {
			specificationList: [
				{
					value: dates,
					operation : "DATES"
				}
			],
			globalOperator : "AND",
			pageRequestDTO : {
				pageNo : pageNum,
				pageSize : 10,
				sort : "ASC",
				sortByColumn : "id"
			}
		};
		const fetchOptions = {
			headers: {
				"Content-Type": "application/json",
			},
			method: "post",
			body: JSON.stringify(reqBody),
		};
		fetch("https://localhost:8080/search/", fetchOptions)
		.then((response) => response.json())
		.then(response => {
			console.log(response);
            setRentals(response.content);
            setLoading(false);
		})
	}, [pageNum]);

    const nextPage = () => {
        if (pageNum + 1 < maxPage) setPageNum(pageNum + 1);
    };

    const previousPage = () => {
        if (pageNum > 0) setPageNum(pageNum - 1);
    };

    const firstPage = () => {
        setPageNum(0);
    };

    const lastPage = () => {
        setPageNum(maxPage - 1);
    };

	if (loading === true){
		return (<h1>Loading...</h1>);
	}

	return (
		<>
			<h1> Rental list : </h1>
            <div>
                <ul>
                    {rentals.map((data) => (
                    <li key={data.id}>
                        <p><a href={"https://localhost:3000/search/" + data.id + "/details"}>{data.title}</a></p>
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
            <div> <a href = 'https://localhost:3000/home'>Homepage</a> </div>
		</>
	);
}

export default SearchResults;