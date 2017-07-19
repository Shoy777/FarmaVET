using ASP.NET_MVC.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ASP.NET_MVC.Controllers
{
    public class VentaController : Controller
    {
        private FarmaVETEntities db = new FarmaVETEntities();

        public ActionResult Index()
        {
            /*
            var orderView = new OrderView();
            orderView.medicamentoOrder = new List<MedicamentoOrder>();
            Session["orderView"] = orderView;
            */
            List<Boleta> data = db.Boleta.Take(5).OrderByDescending(x => x.BoletaId).ToList();
            return View(data);
        }

        //
        // GET: /Venta/
        public ActionResult NewOrder()
        {
            var orderView = new OrderView();
            orderView = Session["orderView"] as OrderView;
            return View(orderView);
        }

        [HttpPost]
        public ActionResult NewOrder(OrderView orderView, string Cliente)
        {
            using (var transaction = db.Database.BeginTransaction())
            {
                try
                {
                    orderView = Session["orderView"] as OrderView;
                    orderView.Cliente = Cliente;
                    if (Cliente.Equals(""))
                    {
                        ViewBag.MessageError = "Ingrese un cliente";
                        return View(orderView);
                    }
                    if (orderView.medicamentoOrder.Count == 0)
                    {
                        ViewBag.MessageError = "Agregue detalle";
                        return View(orderView);
                    }
                    var order = new Boleta
                    {
                        Cliente = orderView.Cliente,
                        FechaVenta = DateTime.Now,
                        MontoTotal = orderView.MontoTotal,
                        CantidadItems = orderView.medicamentoOrder.Count,
                    };
                    db.Boleta.Add(order);
                    db.SaveChanges();

                    foreach (var item in orderView.medicamentoOrder)
                    {
                        var detalle = new DetalleBoleta
                        {
                            BoletaId = order.BoletaId,
                            Cantidad = item.Cantidad,
                            MedicamentoId = item.MedicamentoId,
                            Precio = item.Precio,
                            SubTotal = item.SubTotal
                        };
                        db.DetalleBoleta.Add(detalle);
                        int stock = db.Medicamento.Find(item.MedicamentoId).Stock;
                        if(stock >= item.Cantidad){
                            db.SP_ReducirStockMedicamento(item.MedicamentoId, item.Cantidad);
                        }
                        else if(stock == 0)
                        {
                            orderView.medicamentoOrder.Remove(item);
                            transaction.Rollback();
                            ViewBag.MessageError = "Medicamento"+ string.Format(" {0} {1} {2} removido, porque ya no tiene existencias",
                                item.Nombre, item.DescripcionEspecie, item.DescripcionTipo);
                            return View("NewOrder", orderView);
                        }
                        else
                        {
                            transaction.Rollback();
                            ViewBag.MessageError = "Medicamento" + string.Format(" {0} {1} {2} tiene {3} y se quiere comprar {4}",
                                item.Nombre, item.DescripcionEspecie, item.DescripcionTipo, stock, item.Cantidad);
                            return View("NewOrder", orderView);
                        }
                        db.SaveChanges();
                    }

                    transaction.Commit();

                    ViewBag.MessageSuccess = string.Format("La orden: {0}, fue registrada", order.BoletaId);

                    orderView = new OrderView();
                    orderView.medicamentoOrder = new List<MedicamentoOrder>();
                    Session["orderView"] = orderView;

                    return View("NewOrder", orderView);
                }
                catch(NullReferenceException)
                {
                    orderView = new OrderView();
                    orderView.medicamentoOrder = new List<MedicamentoOrder>();
                    Session["orderView"] = orderView;
                    transaction.Rollback();
                    ViewBag.MessageError = "Debe agregar detalle";
                    return View(orderView);
                }
                catch (Exception e)
                {
                    transaction.Rollback();
                    ViewBag.MessageError = e.Message;
                    return View(orderView);
                }
            }
        }

        public ActionResult AddMedicamento()
        {
            ViewBag.Medicamentos = new SelectList(db.Medicamento, "MedicamentoId", "MedicamentoEspecie");
            return View();
        }

        [HttpPost]
        public ActionResult AddMedicamento(MedicamentoOrder m, int Cantidad)
        {
            ViewBag.Medicamentos = new SelectList(db.Medicamento, "MedicamentoId", "MedicamentoEspecie");
            var MedicamentoId = int.Parse(Request["MedicamentoId"]);
            var orderView = Session["orderView"] as OrderView;

            try
            {
                var medicamento = db.Medicamento.Find(MedicamentoId);
                if (MedicamentoId < 1)
                {
                    ViewBag.MessageError = "Seleccione un producto";
                    return View(m);
                }
                if (medicamento == null)
                {
                    ViewBag.MessageError = "Producto no existe";
                    return View(m);
                }
                if (Cantidad <= 0)
                {
                    ViewBag.MessageError = "Ingrese una cantidad mayor a 0";
                    return View(m);
                }
                if (medicamento.Stock < Cantidad)
                {
                    ViewBag.MessageError = "Medicamento no tiene stock suficiente";
                    return View(m);
                }
                if (medicamento.Stock == 0)
                {
                    ViewBag.MessageError = "Medicamento no tiene existencias";
                    return View(m);
                }

                m = orderView.medicamentoOrder.Find(x => x.MedicamentoId == MedicamentoId);

                if (m == null)
                {
                    m = new MedicamentoOrder
                    {
                        Nombre = medicamento.Nombre,
                        DescripcionEspecie = medicamento.Especie.Descripcion,
                        DescripcionTipo = medicamento.TipoMedicamento.Descripcion,
                        MedicamentoId = medicamento.MedicamentoId,
                        SubTotal = medicamento.Precio * Cantidad,
                        Precio = medicamento.Precio,
                        Cantidad = Cantidad
                    };
                    orderView.medicamentoOrder.Add(m);
                    orderView.MontoTotal += m.SubTotal;
                }
                else
                {
                    m.Cantidad += Cantidad;
                    m.SubTotal += medicamento.Precio * Cantidad;
                    orderView.MontoTotal += medicamento.Precio * Cantidad;
                }

                ViewBag.MessageSuccess = "Se agrego producto";
                return View("NewOrder", orderView);
            }
            catch (NullReferenceException)
            {
                orderView = new OrderView();
                orderView.medicamentoOrder = new List<MedicamentoOrder>();
                Session["orderView"] = orderView;
                ViewBag.MessageError = "Ya puede agregar detalle";
                return View(m);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(m);
            }
        }

        public ActionResult EditMedicamento(int? id)
        {
            var orderView = Session["orderView"] as OrderView;
            var data = orderView.medicamentoOrder.Find(x => x.MedicamentoId == id);
            return View(data);
        }

        [HttpPost]
        public ActionResult EditMedicamento(MedicamentoOrder m, int Cantidad)
        {
            var MedicamentoId = int.Parse(Request["MedicamentoId"]);

            var orderView = Session["orderView"] as OrderView;

            var medicamento = db.Medicamento.Find(MedicamentoId);

            m = orderView.medicamentoOrder.Find(x => x.MedicamentoId == medicamento.MedicamentoId);

            if (MedicamentoId < 1)
            {
                ViewBag.MessageError = "Seleccione un producto";
                return View(m);
            }
            if (medicamento == null)
            {
                ViewBag.MessageError = "Producto no existe";
                return View(m);
            }
            if (Cantidad <= 0)
            {
                ViewBag.MessageError = "Ingrese una cantidad mayor a 0";
                return View(m);
            }
            if (medicamento.Stock < Cantidad)
            {
                ViewBag.MessageError = "Medicamento no tiene stock suficiente";
                return View(m);
            }
            if (medicamento.Stock == 0)
            {
                ViewBag.MessageError = "Medicamento no tiene existencias";
                return View(m);
            }

            orderView.MontoTotal -= medicamento.Precio * m.Cantidad;
            m.Cantidad = Cantidad;
            m.SubTotal = medicamento.Precio * Cantidad;
            orderView.MontoTotal += medicamento.Precio * Cantidad;

            ViewBag.MessageSuccess = "Se edito producto";
            return View("NewOrder", orderView);
        }

        public ActionResult DeleteMedicamento(int? id)
        {
            var orderView = Session["orderView"] as OrderView;
            var data = orderView.medicamentoOrder.Find(x => x.MedicamentoId == id);
            return View(data);
        }

        [HttpPost]
        public ActionResult DeleteMedicamento(MedicamentoOrder m)
        {
            var MedicamentoId = int.Parse(Request["MedicamentoId"]);

            var orderView = Session["orderView"] as OrderView;

            m = orderView.medicamentoOrder.Find(x => x.MedicamentoId == MedicamentoId);

            orderView.MontoTotal -= m.Precio * m.Cantidad;
            orderView.medicamentoOrder.Remove(m);

            ViewBag.MessageSuccess = "Se elimino producto";
            return View("NewOrder", orderView);
        }

        public ActionResult ListadoVentas()
        {
            List<Boleta> data = db.Boleta.Take(5).OrderByDescending(x => x.BoletaId).ToList();
            return View(data);
        }
        public ActionResult _DetalleVenta(int id)
        {
            List<DetalleBoleta> data = db.DetalleBoleta.Where(x => x.BoletaId == id).ToList();
            return PartialView(data);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
	}
}