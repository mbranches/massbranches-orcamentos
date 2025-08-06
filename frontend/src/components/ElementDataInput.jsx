function ElementDataInput({ throwableError, ...rest }) {
    return (
        <div>
            <input
                type="text"
                className={`border rounded px-2 py-1 w-full outline-none ${throwableError ? "border-red-600" : "border-gray-300"}`}
                {...rest}
            />  

            {throwableError && <p className='text-red-600 text-xs font-normal'>{throwableError}</p>}
        </div>
    );
}

export default ElementDataInput;