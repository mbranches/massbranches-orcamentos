function Card({ backgroud, children }) {
    return (
        <div className={`${backgroud} p-6 flex flex-col text-lg rounded-lg`}>
            {children}
        </div>
    );
}

export default Card;