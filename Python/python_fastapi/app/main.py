from fastapi import FastAPI
from app.produto.produtoController import router as produto_router
from app.venda.vendaController import router as venda_router

app = FastAPI()

app.include_router(produto_router)
app.include_router(venda_router)
