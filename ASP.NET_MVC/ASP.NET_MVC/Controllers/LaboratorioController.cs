using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using ASP.NET_MVC.Models;
using System.Data.Entity.Infrastructure;

namespace ASP.NET_MVC.Controllers
{
    public class LaboratorioController : Controller
    {
        private FarmaVETEntities db = new FarmaVETEntities();

        // GET: /Laboratorio/
        public ActionResult Index()
        {
            return View(db.Laboratorio.ToList().OrderByDescending(l => l.LaboratorioId));
        }

        // GET: /Laboratorio/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Laboratorio laboratorio = db.Laboratorio.Find(id);
            if (laboratorio == null)
            {
                return HttpNotFound();
            }
            return View(laboratorio);
        }

        // GET: /Laboratorio/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: /Laboratorio/Create
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include="LaboratorioId,Descripcion")] Laboratorio laboratorio)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    db.Laboratorio.Add(laboratorio);
                    db.SaveChanges();
                    ViewBag.MessageSuccess = "Registro Grabado";
                    return View();
                }
                return View(laboratorio);
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("DescripcionLaboratorioUK"))
                {
                    ViewBag.MessageError = "Ya existe laboratorio con el mismo nombre";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo grabar registro!";
                }
                return View(laboratorio);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(laboratorio);
            }
        }

        // GET: /Laboratorio/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Laboratorio laboratorio = db.Laboratorio.Find(id);
            if (laboratorio == null)
            {
                return HttpNotFound();
            }
            return View(laboratorio);
        }

        // POST: /Laboratorio/Edit/5
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include="LaboratorioId,Descripcion")] Laboratorio laboratorio)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    db.Entry(laboratorio).State = EntityState.Modified;
                    db.SaveChanges();
                    ViewBag.MessageSuccess = "Registro Modificado";
                    return View();
                }
                return View(laboratorio);
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("DescripcionLaboratorioUK"))
                {
                    ViewBag.MessageError = "No puedes editar el nombre a uno existente!";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo editar registro!";
                }
                return View(laboratorio);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(laboratorio);
            }
        }

        // GET: /Laboratorio/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Laboratorio laboratorio = db.Laboratorio.Find(id);
            if (laboratorio == null)
            {
                return HttpNotFound();
            }
            return View(laboratorio);
        }

        // POST: /Laboratorio/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            Laboratorio laboratorio = db.Laboratorio.Find(id);
            try
            {
                db.Laboratorio.Remove(laboratorio);
                db.SaveChanges();
                ViewBag.MessageSuccess = "Registro Eliminado";
                return View();
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("FK"))
                {
                    ViewBag.MessageError = "No se puede eliminar porque hay registros enlazados";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo eliminar!";
                }
                return View(laboratorio);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(laboratorio);
            }
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
